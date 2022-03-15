package com.wallet.walletapi.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.walletapi.dto.WalletItemDto;
import com.wallet.walletapi.entity.WalletItem;
import com.wallet.walletapi.enums.TypeEnum;
import com.wallet.walletapi.response.Response;
import com.wallet.walletapi.service.UserWalletService;
import com.wallet.walletapi.service.WalletItemService;

@RestController
@RequestMapping("wallet-item")
public class WalletItemController {
	
	@Autowired
	WalletItemService walletItemService;
	
	@Autowired
	UserWalletService userWalletService;
	
	
	@PostMapping
	public ResponseEntity<Response<WalletItemDto>> create(@Valid @RequestBody WalletItemDto dto, BindingResult result){
		Response<WalletItemDto> response = new Response<WalletItemDto>();
		
		if(result.hasErrors()) {
			result.getAllErrors().forEach(r -> response.getErros().add(r.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		WalletItem wi = walletItemService.save(this.convertDtoToEntity(dto));
		
		response.setData(this.convertEntityToDto(wi));
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping(value = "/{wallet}") //{} = pathVariavel
	public ResponseEntity<Response<Page<WalletItemDto>>> findBetweenDates(@PathVariable("wallet") Long wallet,
	               @RequestParam("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
	               @RequestParam("endDate") @DateTimeFormat(pattern = "dd-MM-yyy") Date endDate,
	               @RequestParam(name ="page", defaultValue = "0") int page) {
		
		Response<Page<WalletItemDto>> response = new Response<Page<WalletItemDto>>();
		Page<WalletItem> items = walletItemService.findBetweenDates(wallet, startDate, endDate, page);
		Page<WalletItemDto> dto = items.map(i -> this.convertEntityToDto(i));
		response.setData(dto);
		return ResponseEntity.ok().body(response);
	            	   
    }
	
	@GetMapping(value = "/type/{wallet}")
	public ResponseEntity<Response<List<WalletItemDto>>> findByWalletIdAndType(@PathVariable("wallet") Long wallet,
			       @RequestParam("type") String type) {
		
		Response<List<WalletItemDto>> response = new Response<List<WalletItemDto>>();
		List<WalletItem> list = walletItemService.findByWalletAndType(wallet, TypeEnum.getEnum(type));
		
		List<WalletItemDto> dto = new ArrayList<>();
		list.forEach(i -> dto.add(this.convertEntityToDto(i)));
		response.setData(dto);
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(value = "/total/{wallet}")
	public ResponseEntity<Response<BigDecimal>> sumByWalletId(@PathVariable("wallet") Long wallet) {
		
		Response<BigDecimal> response = new Response<BigDecimal>();
		BigDecimal value = walletItemService.sumWalletId(wallet);
		response.setData(value == null ? BigDecimal.ZERO : value);
		                                //retorna o ou entaõ retorna o value do servico
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<Response<WalletItemDto>> update(@Valid @RequestBody WalletItemDto dto, BindingResult result) {
		
		Response<WalletItemDto> response = new Response<WalletItemDto>();
		
		Optional<WalletItem> wi = walletItemService.findById(dto.getId());// busacando o id do dto no banco
		// como no optinal pode ou não ter se faz a conferencia
		if(!wi.isPresent()) {     // senão estiver presente se adiciona um erro9 instacia com new          
			result.addError(new ObjectError("WalletItem","WalletItem não encontardo"));// o nme da msg e a descricao
		} else if (wi.get().getWallet().getId().compareTo(dto.getWallet()) != 0) { // pegando o id do Wallet(wi) que esta salvo no banco e comparando com o Id do Dto , se for iagual a 0 ok senao retorna um erro 
				result.addError(new ObjectError("WalletItemChanged", "Voce Não pode alterar a carteira"));   //criando mais um novo erro                                  
		}
		if(result.hasErrors()) { //se houver erro retorna para o usuario no response
			result.getAllErrors().forEach(r -> response.getErros().add(r.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		//salavando o dto vindo no payload e retrona o saved para o usuario
		WalletItem saved = walletItemService.save(this.convertDtoToEntity(dto));
		
		response.setData(this.convertEntityToDto(saved));
		return ResponseEntity.ok().body(response);
	}
	@DeleteMapping(value = "/{walletItemId}")
	public ResponseEntity<Response<String>> delete(@PathVariable("wallet") Long walletItemId) {
		Response<String> response = new Response<String>();
		
		Optional<WalletItem> wi = walletItemService.findById(walletItemId); // buscando no banco de dados
		
		if(!wi.isPresent()) {
			response.getErros().add("Carteira de id" + walletItemId + "não encontrada");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		
		walletItemService.deleteById(walletItemId);
		response.setData("Carteira de id" + walletItemId + "apagada com sucesso");
		return ResponseEntity.ok().body(response);
	}
	


	private WalletItem convertDtoToEntity(WalletItemDto dto) {
		
		WalletItem wi = new WalletItem();
		wi.setDate(dto.getDate());
		wi.setDescripition(dto.getDescription());
		wi.setId(dto.getId());
		wi.setType(TypeEnum.getEnum(dto.getType()));
		wi.setValue(dto.getValue());
		
		return wi;
	}
	
	private WalletItemDto convertEntityToDto(WalletItem wi) {
		
		WalletItemDto dto = new WalletItemDto();
		dto.setDate(wi.getDate());
		dto.setDescription(wi.getDescripition());
		dto.setId(wi.getId());
		dto.setType(wi.getType().getValue());
		dto.setValue(wi.getValue());
		
		return dto;
	}

}
