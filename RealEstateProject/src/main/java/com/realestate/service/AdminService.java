package com.realestate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realestate.domain.Material;
import com.realestate.domain.SecurityQuestions;
import com.realestate.domain.Status;
import com.realestate.domain.UOM;
import com.realestate.domain.User;
import com.realestate.exceptions.CustomException;
import com.realestate.repository.MaterialRepository;
import com.realestate.repository.SecurityQuestionsRepository;
import com.realestate.repository.StatusRepository;
import com.realestate.repository.UOMRepository;
import com.realestate.repository.UserRepository;

@Service
public class AdminService {

	@Autowired
	private SecurityQuestionsRepository sqRepo;

	@Autowired
	private MaterialRepository materialRepo;

	@Autowired
	private UOMRepository uomRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	public StatusRepository statusRepo;

	public SecurityQuestions addSqecurityQuestions(SecurityQuestions sq) throws CustomException {
		String q=sqRepo.findByQuestion(sq.getQuestion());
		if(q!=null)
			throw new CustomException("Question already registerd.");
		return sqRepo.save(sq);
	}

	public Material addMaterial(Material m) throws CustomException {
		String name=materialRepo.findByMaterialByName(m.getName());
		if(name!=null)
			throw new CustomException("Material already registerd.");
		
		return materialRepo.save(m);
	}

	/*public UOM addUom(Map<String, Object> map, String jsonKeys[])throws CustomException {
	
		Material m=materialRepo.findOne((Integer) map.get(jsonKeys[1]));
		if(m==null){
			throw new CustomException("Material details not found");
		}
		UOM u=new UOM();
		u.setMaterial(m);
		u.setName((String)map.get(jsonKeys[0]));
		
		return uomRepo.save(u);
	
	}*/
	
	public Integer changeUserStatus(Map<String, Object> map, String jsonKeys[])throws CustomException 
	{
		User user = userRepo.findByUsername((String)map.get(jsonKeys[0]));
		if (user == null)
			throw new CustomException("User details not found");
		if (user.getStatus() == null)
			throw new CustomException("User details with no Status found");

		Status s=statusRepo.findByStatus((String)map.get(jsonKeys[1]));
		System.out.println(s.getStatus()+"***********************");
		if(s==null){
			throw new CustomException("Status details not found");
		}
		user.setStatus(s);
		User uu=userRepo.save(user);
		//Integer i = null;
			//i = userRepo.changeStatus(s.getStatus(), (String)map.get(jsonKeys[0]));		
		return (uu.getStatus().getStatus().equals("Approved") ? 1 : 0 );
	}

	public Integer addUom(Integer mid , List<String> uoms) throws CustomException {
		
		Material m=materialRepo.findOne(mid);
		if(m==null){
			throw new CustomException("Material details not found");
		}
		List<UOM> uomList=new ArrayList<UOM>();
		for(String name : uoms){
		UOM u=new UOM();		
		u.setName(name);
		uomList.add(u);
		}
		m.setUom(uomList);
		Material mm= materialRepo.save(m);
		if(mm.getUom().size() >= uomList.size())
			return 1;
		else return 0;
	}
}
