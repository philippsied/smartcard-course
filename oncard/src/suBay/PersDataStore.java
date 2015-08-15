package suBay;

import javacard.framework.ISO7816;
import javacard.framework.Util;

public class PersDataStore {

byte [] fname;
	static byte [] surname;
	static byte [] bDay;
	static byte [] location;
	static byte [] street;
	static byte [] phoneNumber;
	static byte [] pic;
	  
	PersDataStore(){
	  fname = new byte[10];
	  surname = new byte[10];
	  bDay = new byte[10];
	  location = new byte[15];
	  street = new byte[25];
	  phoneNumber = new byte[15];
	  pic = new byte[6750];
	}
	  
	public byte[] getFName(){
	  return(fname);
	}
	
	public byte[] getSurName(){
	  return(surname);
	}
	
	public byte[] getBDay(){
	  return(bDay);
	}
	
	public byte[] getLocation(){
	  return(location);
	}
	
	public byte[] getStreet(){
	  return(street);
	}
	
	public byte[] getPhoneNumber(){
	  return(phoneNumber);
	}
	
	public byte[] getPic(){
	  return(pic);
	} 
	
	public void setPicPart(byte [] buf, short butOff, short picOff ,short length){
	  Util.arrayCopy(buf, butOff , pic, picOff, length);
	} 
}

