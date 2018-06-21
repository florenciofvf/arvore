package br.com.arvore.titulo;

public interface TituloListener {
	public void clonarLocalDivisor(Titulo titulo);

	public void excluirFichario(Titulo titulo);

	public void cloneEsquerdo(Titulo titulo);

	public void cloneDireito(Titulo titulo);

	public void cloneAbaixo(Titulo titulo);

	public void selecionar(Titulo titulo);

	public void cloneAcima(Titulo titulo);

	public void excluirAba(Titulo titulo);

	public void clonarAba(Titulo titulo);

	public void restaurar(Titulo titulo);

	public void maximizar(Titulo titulo);

	public void renomear(Titulo titulo);
}