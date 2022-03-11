package com.wallet.walletapi.enums;

public enum TypeEnum {
	
	EN("ENTRADA"),
	SD("SAIDA");
			
   private final String value; // declrando o retorno statico
   
   TypeEnum(String value){ // sertando no construtor o valor recebido no value
	   this.value = value;
   }
   
   public String getValue() { // retorna no get o valor
	   return this.value;
   }
   
   public static TypeEnum getEnum(String value) { // revebe um valor no value
	   for(TypeEnum t : values()) { // verifica se exixte o valor recebido
		   if(value.equals(t.getValue())) {
			   return t; // retorna esse valor para a string value
		   }
		   
	   }
	   return null; // caso nao encontrar retrona nulo
	   
   }
}
