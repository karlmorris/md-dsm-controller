package dsvm.generator;

public abstract class PostModelGenerationOperation {
	Object data;
	public PostModelGenerationOperation(Object data){
		this.data = data;
	}
	public abstract boolean execute ();
}
