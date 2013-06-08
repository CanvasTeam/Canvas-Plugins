package com.zombiehippie.bukkit.claims;

public class Claim {
	protected String ownerName;
    
	protected int id = 0;
	
	protected int northBoundary;
	protected int southBoundary;
	protected int eastBoundary;
	protected int westBoundary;

	
	/**
	 * Construct a Claim
	 */
	public Claim(){
		// No construction needed
	}
	
	public void assignUniqueId(){
		// Find a unique id
		int new_id = 0;
		boolean found_id = false;
		
		while(!found_id) {
			new_id = (int)(Math.random() * 99999);
			found_id = !CanvasClaims.getClaimIds().contains(new_id);
		}
		
		setId(new_id);
	}
	
	public void setId(int new_id){
		this.id = new_id;
	}
	
	public int getId() {
		return id;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getNorthBoundary() {
		return northBoundary;
	}

	public void setNorthBoundary(int northBoundary) {
		this.northBoundary = northBoundary;
	}

	public int getSouthBoundary() {
		return southBoundary;
	}

	public void setSouthBoundary(int southBoundary) {
		this.southBoundary = southBoundary;
	}

	public int getEastBoundary() {
		return eastBoundary;
	}

	public void setEastBoundary(int eastBoundary) {
		this.eastBoundary = eastBoundary;
	}

	public int getWestBoundary() {
		return westBoundary;
	}

	public void setWestBoundary(int westBoundary) {
		this.westBoundary = westBoundary;
	}
	
	/**
	 * Construct a Claim with a username and any two points
	 * @param Username the owner of the Claim
	 * @param x1 an x-boundary
	 * @param y1 a y-boundary
	 * @param x2 an x-boundary
	 * @param y2 a y-boundary
	 */
	public void setupClaim(String UserName, int x1, int y1, int x2, int y2) {
		setBoundaries(x1, y1, x2, y2);
		this.ownerName = UserName;
	}
	
	/**
	 * Construct an Area with any two points
	 * @param x1 an x-boundary
	 * @param z1 a z-boundary
	 * @param x2 an x-boundary
	 * @param z2 a z-boundary
	 */
	private void setBoundaries(int x1, int z1, int x2, int z2){
		setNorthBoundary(z1>z2?z1:z2);
		setSouthBoundary(z1<z2?z1:z2);
		setEastBoundary(x1>x2?x1:x2);
		setWestBoundary(x1<x2?x1:x2);
	}
	
	/**
	 * Check to see if two areas are intersecting
	 * @param area Area to check against
	 * @return boolean if intersecting
	 */
	public boolean isIntersecting(Claim area){
        // intersections based off of Rectangle class intersections
        return ((area.getEastBoundary() > this.getWestBoundary()) &&
                (area.getNorthBoundary() > this.getSouthBoundary()) &&
                (this.getEastBoundary() > area.getWestBoundary()) &&
                (this.getNorthBoundary() > area.getSouthBoundary()));
	}
	
	/**
	 * Check if given X and Z coords fall inside the area
	 * @param X
	 * @param Z 
	 * @return is the point in the area?
	 */
	public boolean isInside(int X, int Z){
		// inside checks
		return ((X <= this.getEastBoundary()) &&
				(X >= this.getWestBoundary()) &&
				(Z <= this.getNorthBoundary()) &&
				(Z >= this.getSouthBoundary()));
	}
	
	public boolean ownsClaim(String UserName){
		return ownerName.equalsIgnoreCase(UserName);
	}
	
	/**
	 * Returns the calculated width of the Area
	 * @return the width
	 */
	public int getWidth(){
		return this.getEastBoundary() - this.getWestBoundary();
	}
	
	/**
	 * Returns the calculated height of the Area
	 * @return the height
	 */
	public int getHeight(){
		return this.getNorthBoundary() - this.getSouthBoundary();
	}
}
