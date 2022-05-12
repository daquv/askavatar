package com.daquv.Controller;



import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.daquv.Service.IndexService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/api/v1/askavatar")
public class IdexController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IndexService indexService;
	
	@GetMapping("")
	public String index(HttpServletRequest request,
									   HttpServletResponse response,
									   @RequestParam HashMap param,
									   HttpSession session) throws Exception
	{
		return "hi";
	}
	


	@GetMapping(value="/convertToString")
	public String convertToString(HttpServletRequest request,
								  HttpServletResponse response,
								  @RequestParam HashMap param,
								  HttpSession session) throws Exception
	{
        String strResult="";

	    try
        {
            strResult =  indexService.convertToString();


        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return strResult;


	}
}
