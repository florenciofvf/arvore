package br.com.arvore.fichario;

public interface TituloListener {
	public void clonarLocalDivisor(Titulo titulo);

	public void selecionarObjeto(Titulo titulo);

	public void excluirFichario(Titulo titulo);

	public void cloneEsquerdo(Titulo titulo);

	public void cloneDireito(Titulo titulo);

	public void cloneAbaixo(Titulo titulo);

	public void cloneAcima(Titulo titulo);

	public void excluirAba(Titulo titulo);

	public void clonarAba(Titulo titulo);
}