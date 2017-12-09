package com.realestate.util;

public enum EnumConstants {

	// signin
	
	INVALID_USER {
	      public String toString() {
	          return "{\"message\" : \"User details not found \" , \"result\" : false}";
	      }
	  },
	 
	  INVALID_PASSWORD {
	      public String toString() {
	          return "{\"message\" : \"Please enter  Correct Password\" ,\"result\" : false}";
	      }
	  },
	  
	  // empty input request body
	  
	  EMPTY_JSON_STRING{
		  public String toString() {
	          return "{\"message\" : \"Input Request has empty Values\" ,\"result\" : false}";
	      }
	  },
	  
	  //order
	  
	  ORDER_NOT_CREATED{
		  public String toString() {
	          return "{\"message\" : \"Order is not created\" ,\"result\" : false}";
	      }
	  },
	  
	  ORDER_CREATED{
		  public String toString() {
	          return "{\"message\" : \"Order created Successfully\" ,\"result\" : true}";
	      }
	  },
	  
	  ORDER_UPDATED{
		  public String toString() {
	          return "{\"message\" : \"Order details updated Successfully\" ,\"result\" : true}";
	      }
	  },
	  
	  ORDER_NOT_FOUND{
		  public String toString() {
	          return "{\"message\" : \"Order details not Found\" ,\"result\" : false}";
	      }
	  },
	  
	  // builder
	  
	  USER_CREATED{
		  public String toString() {
	          return "{\"message\" : \"User registered Successfully\" ,\"result\" : true}";
	      }
	  },
	  
	  USER_NOT_CREATED{
		  public String toString() {
	          return "{\"message\" : \"User not registered \" ,\"result\" : false}";
	      }
	  },
	  
	  USER_NOT_FOUND{
		  public String toString() {
	          return "{\"message\" : \"User details not Found \" ,\"result\" : false}";
	      }
	  },
	  
	  //general 
	  
	  SUUCESS_INFO{
		  public String toString() {
	          return "{\"result\" : true}";
	      }
	  },
	  
	  FAILURE_INFO{
		  public String toString() {
	          return "{\"result\" : false}";
	      }
	  },
	  
	  //invoice
	    
	  INVOICE_CREATED{
		  public String toString() {
	          return "{\"message\" : \"Order details saved suucessfully, and Invoice generated\" ,\"result\" : true}";
	      }
	  },
	  
	  INVOICE_NOT_CREATED{
		  public String toString() {
	          return "{\"message\" : \"Invoice not created\" ,\"result\" : false}";
	      }
	  }
	  ,
	  
	  INVOICE_NOT_FOUND{
		  public String toString() {
	          return "{\"message\" : \"Invoice details not found\" ,\"result\" : false}";
	      }
	  }
	  ,
	  
	  //secret question
	  
	  SECURITY_QUESTION_CREATED{
		  public String toString() {
	          return "{\"message\" : \"Secret Question created Successfully\" ,\"result\" : true}";
	      }
	  },
	  
	  SECURITY_QUESTION_NOT_CREATED{
		  public String toString() {
	          return "{\"message\" : \"Secret Question not created\" ,\"result\" : false}";
	      }
	  }
	  ,
	  
	  SECURITY_QUESTION_NOT_FOUND{
		  public String toString() {
	          return "{\"message\" : \"Secret Question details not found\" ,\"result\" : false}";
	      }
	  }
	  ,
	  
	  SECURITY_PWSSWORD_NOT_MATCHED{
		  public String toString() {
	          return "{\"message\" : \"Secret Question Password not matched\" ,\"result\" : false}";
	      }
	  },
	  
	  //user_result
	  
	  USER_NULL_STATUS{
		  public String toString() {
	          return "{\"message\" : \"User does not have any status yet\" ,\"result\" : false}";
	      }
	  },
	  
	  USER_STATUS_UPDATED{
		  public String toString() {
	          return "{\"message\" : \"User status updated Successfully\" ,\"result\" : true}";
	      }
	  },
	  
	  USER_STATUS_NOT_UPDATED{
		  public String toString() {
	          return "{\"message\" : \"User status not updated\" ,\"result\" : false}";
	      }
	  },
	  
	  
	  // uri
	  
	  URI_NOT_FOUND{
		  public String toString() {
	          return "{\"message\" : \"Requested URI not found\" ,\"result\" : false}";
	      }
	  },
	  
	  
	  //password
	  
	  PASSWORD_INCORRECT{
		  public String toString() {
	          return "{\"message\" : \"Incorrect old password\" ,\"result\" : false}";
	      }
	  },
	  
	  PASSWORD_CHANGE_SUCCESS{
		  public String toString() {
	          return "{\"message\" : \"Password details updated Successfully\" ,\"result\" : true}";
	      }
	  },
	  
	  PASSWORD_CHANGE_FAILED{
		  public String toString() {
	          return "{\"message\" : \"Password details not updated\" ,\"result\" : false}";
	      }
	  },
	  
	  
	  //material
	  
	  MATERIAL_CREATED{
		  public String toString() {
	          return "{\"message\" : \"Material details added successfully\" ,\"result\" : true}";
	      }
	  },
	  
	  MATERIAL_NOT_CREATED{
		  public String toString() {
	          return "{\"message\" : \"Material details  not added \" ,\"result\" : false}";
	      }
	  },
	  
	  
	  MATERIAL_NOT_FOUND{
		  public String toString() {
	          return "{\"message\" : \"Material details not found \" ,\"result\" : false}";
	      }
	  },
	  
	  
	  //uom
	  
	  
	  UOML_CREATED{
		  public String toString() {
	          return "{\"message\" : \"UOM details added successfully\" ,\"result\" : true}";
	      }
	  },
	  
	  UOM_NOT_CREATED{
		  public String toString() {
	          return "{\"message\" : \"UOM details not added \" ,\"result\" : false}";
	      }
	  },
	  
	  UOM_NOT_FOUND{
		  public String toString() {
	          return "{\"message\" : \"UOM details not found \" ,\"result\" : false}";
	      }
	  },
	  
	  ;
	  
	  
	
	  
	  public String getresult() {
	        return this.toString();
	    }
	
}
