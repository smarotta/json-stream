package com.avenuecode.talk.stream.reader.controller.view;

public interface View<M, L> {

	void initialize();
	
	void setListener(L listener);
	
	void update(M model);
}
