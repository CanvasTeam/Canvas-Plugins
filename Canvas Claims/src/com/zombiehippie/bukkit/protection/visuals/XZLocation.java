package com.zombiehippie.bukkit.protection.visuals;

public class XZLocation {
	public int x;
	public int z;
	public XZLocation(int _x, int _z){
		x = _x;
		z = _z;
	}
	public XZLocation getRelative(int dx, int dz){
		return new XZLocation(x+dx,z+dz);
	}
	public int getCode(){
		return x*10000+z;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof XZLocation){
			XZLocation xz =(XZLocation) o;
			if(this.x == xz.x && this.z == xz.z)
				return true;
		}
		return false;
	}
}
