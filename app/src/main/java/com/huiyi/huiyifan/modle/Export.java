package com.huiyi.huiyifan.modle;


/**
 * 导出
 * */

public class Export {
	
	private Integer ID;
	private Integer xma;
	private Integer zma;
	private Integer dma;
	private Integer sima;
	private Integer wuma;

	public Export(Integer xma, Integer zma, Integer dma, Integer sima, Integer wuma) {
		super();
		this.xma = xma;
		this.zma = zma;
		this.dma = dma;
		this.sima = sima;
		this.wuma = wuma;
	}
	
	public Export(Integer iD, Integer xma, Integer zma, Integer dma, Integer sima, Integer wuma) {
		super();
		ID = iD;
		this.xma = xma;
		this.zma = zma;
		this.dma = dma;
		this.sima = sima;
		this.wuma = wuma;
	}
	
	
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	
	public Integer getXma() {
		return xma;
	}
	public void setXma(Integer xma) {
		this.xma = xma;
	}
	
	public Integer getZma() {
		return zma;
	}
	public void setZma(Integer zma) {
		this.zma = zma;
	}
	
	public Integer getDma() {
		return dma;
	}
	public void setDma(Integer dma) {
		this.dma = dma;
	}
	
	public Integer getSima() {
		return sima;
	}
	public void setSima(Integer sima) {
		this.sima = sima;
	}
	
	public Integer getWuma() {
		return wuma;
	}
	public void setWuma(Integer wuma) {
		this.wuma = wuma;
	}


	
	
	

}
