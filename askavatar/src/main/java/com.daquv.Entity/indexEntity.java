package com.daquv.Entity;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class indexEntity {
	
	 private Integer id;
	    private String userid;
	    private String acttype;
	    private String clientip;
	    private String status;
	    private String menuname;
	    private String text;
	    private Date crtdate;

}
