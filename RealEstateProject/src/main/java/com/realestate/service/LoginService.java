package com.realestate.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.catalina.startup.UserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.realestate.domain.Material;
import com.realestate.domain.SecurityQuestions;
import com.realestate.domain.User;
import com.realestate.domain.UserInfo;
import com.realestate.exceptions.CustomException;
import com.realestate.repository.MaterialRepository;
import com.realestate.repository.SecurityQuestionsRepository;
import com.realestate.repository.StatusRepository;
import com.realestate.repository.UOMRepository;
import com.realestate.repository.UserInfoRepository;
import com.realestate.repository.UserRepository;
import com.realestate.util.EnumConstants;
import com.realestate.util.PasswordEncryption;
import com.realestate.util.UIDGenerator;
import static com.realestate.util.CommonConstants.PENDING;
import static com.realestate.util.CommonConstants.REJECTED;

@Service
@Transactional
public class LoginService {

	@Autowired
	public UserRepository userRepo;

	@Autowired
	public UOMRepository uomRepo;

	@Autowired
	public MaterialRepository materialRepo;

	@Autowired
	public SecurityQuestionsRepository sqRepo;
	
	@Autowired
	public StatusRepository statusRepo;
	
	@Autowired
	private ServletContext servletContext;

	@Autowired
	private UserInfoRepository builderRepo;

	public UserInfo addUser(UserInfo userInfo)
	{
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
	//	List<Material> newMaterialList=null;
		
		String ss=sd.format(new Date());
		Date date=null;
		try {
			date = sd.parse(ss);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		User user=userInfo.getUser_id();
		user.setUser_id(UIDGenerator.uniqueIdGenerator(builderRepo.count()+1L, "User"));
		user.setDoj(date);
		user.setStatus(statusRepo.findOne(PENDING));
		//user.setUser_type("builder");
		user.setUser_name(userInfo.getBusiness_email());
		//user.setSqPwd(PasswordEncryption.encryptPwd(user.getSqPwd()));
		
		//new logic
		if(user.getMaterial()!=null && user.getMaterial().size() > 0)
		{
			List<String> materials=user.getMaterial();
			String mname="";
			for(String m : materials){
				mname=mname+m+",";
			}
			user.setVendor_materials(mname);
		}
				
		/*if(user.getMaterial()!=null && user.getMaterial().size()<0)
		{
			List<Material> userListOfMaterial=user.getMaterial();
			List<Material> dbListOfMaterial=materialRepo.findAllMaterials();
			
			List<Material> union=new ArrayList<Material>(dbListOfMaterial);
			union.addAll(userListOfMaterial);//all dbmaterials + usermaterials 
			
			List<Material> intersection=new ArrayList<Material>(dbListOfMaterial);
			union.retainAll(userListOfMaterial); // usermaterials == dbmaterials //old materials
			
			union.removeAll(intersection); // user new materials + db materials not in usermaterials
			union.retainAll(userListOfMaterial); //newly added materials from user
			newMaterialList=new ArrayList<Material>(union);
			
		}*/
		
		UserInfo b=builderRepo.save(userInfo);
				
		return b;
	}
	
	public Integer forgotPassword(String username, String pwd, String sqPwd) throws CustomException
	{
		User user = userRepo.findByUsername(username);

		if (user == null)
			throw new CustomException(EnumConstants.USER_NOT_FOUND.toString());

		if (!PasswordEncryption.checkPwd(sqPwd, user.getSqPwd()))
			throw new CustomException(EnumConstants.SECURITY_PWSSWORD_NOT_MATCHED.toString());

		Integer res = userRepo.changePassword(PasswordEncryption.encryptPwd(pwd),username);
		return res;

	}
	
	
	public User resetNewPassword(String username, String password, String sqId, String sqPwd) throws CustomException
	{
		User user = userRepo.findByUsername(username);

		if (user == null)
			throw new CustomException(EnumConstants.INVALID_USER.toString());

		SecurityQuestions qu = sqRepo.findOne(Integer.parseInt(sqId));

		if (qu == null)
			throw new CustomException(EnumConstants.SECURITY_QUESTION_NOT_FOUND.toString());

		user.setPassword(PasswordEncryption.encryptPwd(password));
		user.setSqId(qu);
		user.setSqPwd(PasswordEncryption.encryptPwd(sqPwd));

		User u = userRepo.save(user);
		return u;
	}

	public SecurityQuestions getUserSecurityQuestion(String username) throws CustomException {
		
		User res = userRepo.findByUsername(username);

		if (res == null)
			throw new CustomException(EnumConstants.USER_NOT_FOUND.toString());

		if (PENDING.equals(res.getStatus().getStatus()))
			throw new CustomException("{\"message\":\"User not yet approved by Admin\",\"result\" : false}");

		if (REJECTED.equals(res.getStatus().getStatus()))
			throw new CustomException("{\"message\":\"User rejected by Admin\",\"result\" : false}");

		SecurityQuestions seq = res.getSqId();
		if (seq == null)
			throw new CustomException(EnumConstants.SECURITY_QUESTION_NOT_FOUND.toString());
		
		return seq;
	}

	public User getUserByUsername(String username) throws CustomException {
		User res = userRepo.findByUsername(username);

		if (res == null)
			throw new CustomException(EnumConstants.USER_NOT_FOUND.toString());
		
		return res;

	}
}
