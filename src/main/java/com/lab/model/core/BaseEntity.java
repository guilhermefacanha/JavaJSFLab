package com.lab.model.core;

import java.io.Serializable;

import com.lab.util.ModelUtils;

public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 8521720777643264456L;
	
	public abstract Object getId();
	
	public boolean isNew() {
		Object obj = getId();
		if(obj instanceof String) {
			return ModelUtils.isNullEmpty((String) obj);
		}
		else if(obj instanceof Integer) {
			return ModelUtils.isNullZero((Integer) obj);
		}
		return true;
	}

}
