package edu.cg;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class FunctioalForEachLoops {
	private int width;
	private int height;
	
	private class Params {
		public final int width, height;
		
		public Params() {
			width = getForEachWidth();
			height = getForEachHeight();
		}
		
		public void restoreParams() {
			setForEachWidth(width);
			setForEachHeight(height);
		}
	}
	
	private Deque<Params> stack;
	
	public FunctioalForEachLoops() {
		width = height = 0;
		stack = new ArrayDeque<>();
	}
	
	public final void setForEachParameters(int width, int height) {
		setForEachWidth(width);
		setForEachHeight(height);
	}
	
	public final void setForEachWidth(int width) {
		this.width = width;
	}
	
	public final void setForEachHeight(int height) {
		this.height = height;
	}
	
	public final int getForEachWidth() {
		return width;
	}
	
	public final int getForEachHeight() {
		return height;
	}
	
	public final void pushForEachParameters() {
		stack.push(new Params());
	}
	
	public final void popForEachParameters() {
		stack.pop().restoreParams();
	}
	
	public final void forEach(BiConsumer<Integer, Integer> action) {
		forEachHeight(y ->
			forEachWidth(x ->
				action.accept(y, x)
			)
		);
	}
	
	public final void forEachWidth(Consumer<Integer> action) {
		for(int x = 0; x < width; ++x)
			action.accept(x);
	}
	
	public final void forEachHeight(Consumer<Integer> action) {
		for(int y = 0; y < height; ++y)
			action.accept(y);
	}
}
