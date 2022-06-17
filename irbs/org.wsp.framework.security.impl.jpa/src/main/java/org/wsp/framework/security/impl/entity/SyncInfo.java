package org.wsp.framework.security.impl.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class SyncInfo implements Serializable {

	private Integer timestamp;
	private String algo;
	private String mesHmac;
	private String data;
}
