package com.zombiehippie.bukkit.claimfacets;

import com.zombiehippie.bukkit.claims.Claim;

public class BorderedClaim extends Claim{
	private int NorthBorder = 0;
	private int SouthBorder = 0;
	private int EastBorder = 0;
	private int WestBorder = 0;
	
	public BorderedClaim(Claim before){
		this.eastBoundary = before.getEastBoundary();
		this.westBoundary = before.getWestBoundary();
		this.northBoundary = before.getNorthBoundary();
		this.southBoundary = before.getSouthBoundary();
		this.id = before.getId();
		this.ownerName = before.getOwnerName();
	}

	public int getE(){
		return this.eastBoundary;
	}
	public int getW(){
		return this.westBoundary;
	}
	public int getN(){
		return this.northBoundary;
	}
	public int getS(){
		return this.southBoundary;
	}
	
	@Override
	public int getNorthBoundary(){
		return this.northBoundary+NorthBorder;
	}
	@Override
	public int getSouthBoundary(){
		return this.southBoundary+SouthBorder;
	}
	@Override
	public int getEastBoundary(){
		return this.eastBoundary+EastBorder;
	}
	@Override
	public int getWestBoundary(){
		return this.westBoundary+WestBorder;
	}
	
	public int getNorthBorder() {
		return NorthBorder;
	}
	public void setNorthBorder(int northBorder) {
		NorthBorder = northBorder;
	}
	public int getSouthBorder() {
		return SouthBorder;
	}
	public void setSouthBorder(int southBorder) {
		SouthBorder = southBorder;
	}
	public int getEastBorder() {
		return EastBorder;
	}
	public void setEastBorder(int eastBorder) {
		EastBorder = eastBorder;
	}
	public int getWestBorder() {
		return WestBorder;
	}
	public void setWestBorder(int westBorder) {
		WestBorder = westBorder;
	}
}
