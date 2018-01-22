package com.mkath.cursomc.domain.enums;

public enum TipoCliente {

	PESSOA_FISICA(1, "Pessoa Física"),
	PESSOA_JURIDICA(2, "Pessoa Jurídica");
	
	private int codigo;
	
	private String descricao;
	
	private TipoCliente(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public int getCod() {
		return this.codigo;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public static TipoCliente toEnum(Integer codigo) {
		if	(codigo == null) {
			return null;
		}
		
		for	(TipoCliente tp: TipoCliente.values()) {
			if	(codigo.equals(tp.getCod())) {
				return tp;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: "+ codigo);
	}
}
