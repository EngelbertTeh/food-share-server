package org.foodshare.backend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Set;

import org.foodshare.backend.entity.IndividualUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IndividualUserRepositoryTest {
	
	@Autowired
	private IndividualUserRepository indiUserRepo;
	private IndividualUser indiUser;
	private Validator validator;
	
	
	@BeforeEach
	public void setup() {
		indiUser = new IndividualUser();
		indiUser.setEmail("user@domain.com");
		indiUser.setPassword("12345678");
		indiUser.setName("Food Share");
		indiUser.setMobileNo("+6587654321");
		indiUser.setPostcode(601234);
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
	}
	
	@AfterEach
	public void tearDown() {
		indiUserRepo.deleteAll();
		indiUser = null;
	}
	
	@Test
	// Test to ensure, after the entity is persisted, all the fields are valid, e.g., not null if they are supposed to have values, have the right boolean value false/true, have the right starting integer e.g. starts with 0
	public void testRegisterUserAllFieldsValid() {
		IndividualUser userReturned = indiUserRepo.save(indiUser);
		
		Assert.notNull(userReturned, "An individualUser object must be returned");	
		assertEquals(indiUser.getEmail(),userReturned.getEmail());
		assertEquals(indiUser.getPassword(),userReturned.getPassword());
		assertEquals(false,userReturned.getIsDeactivated());
		assertEquals(indiUser.getCreatedAt(),userReturned.getCreatedAt());
		assertNotNull(userReturned.getId());
		assertEquals(indiUser.getName(),userReturned.getName());
		assertEquals(indiUser.getMobileNo(),userReturned.getMobileNo());
		assertEquals(indiUser.getPostcode(),userReturned.getPostcode());
		assertEquals(0,userReturned.getRewardPts()); //user starts of with 0 points
	}
	
	@Test
	// test to make sure validators does what it supposed to do
	public void testMakeSureEmailNamesValid() {
		// valid email
		indiUser.setEmail("validEmail123@gmail.com");
	    Set<ConstraintViolation<IndividualUser>> violationsNone = validator.validate(indiUser);
	    assertEquals(0, violationsNone.size());
	    
	    //invalid emails
	    indiUser.setEmail("HotD!ck@hotmail.com");
	    Set<ConstraintViolation<IndividualUser>> violationsNoFunnyNames = validator.validate(indiUser);
	    assertEquals(1, violationsNoFunnyNames.size());
	    
	    indiUser.setEmail("notValid");
	    Set<ConstraintViolation<IndividualUser>> violationsEmailAndPattern1 = validator.validate(indiUser);
	    assertEquals(2, violationsEmailAndPattern1.size());
	    
	    indiUser.setEmail("@notValid.com");
	    Set<ConstraintViolation<IndividualUser>> violationsEmailAndPattern2 = validator.validate(indiUser);
	    assertEquals(2, violationsEmailAndPattern2.size());
	    
	    indiUser.setEmail("notValid@.com");
	    Set<ConstraintViolation<IndividualUser>> violationsEmailAndPattern3 = validator.validate(indiUser);
	    assertEquals(2, violationsEmailAndPattern3.size());
	    
	    indiUser.setEmail(" ");
	    Set<ConstraintViolation<IndividualUser>> violationsBlank1 = validator.validate(indiUser);
	    assertEquals(3, violationsBlank1.size());
	    
	    indiUser.setEmail("");
	    Set<ConstraintViolation<IndividualUser>> violationsBlank2 = validator.validate(indiUser);
	    assertEquals(2, violationsBlank2.size());
	}
	
	@Test
	public void testMakeSurePasswordValid() {
		//valid passwords
		indiUser.setPassword("12345678");
		Set<ConstraintViolation<IndividualUser>> violationNone1 = validator.validate(indiUser);
		assertEquals(0, violationNone1.size());
		
		indiUser.setPassword("123456789jklmnopqrstuvwxyz!@#$");
		Set<ConstraintViolation<IndividualUser>> violationNone2 = validator.validate(indiUser);
		assertEquals(0, violationNone2.size());
		
		//invalid passwords
		indiUser.setPassword("123");
		Set<ConstraintViolation<IndividualUser>> violationSize1 = validator.validate(indiUser);
		assertEquals(1, violationSize1.size());
		
		indiUser.setPassword("123456789jklmnopqrstuvwxyz!@#$31");
		Set<ConstraintViolation<IndividualUser>> violationSize2 = validator.validate(indiUser);
		assertEquals(1, violationSize2.size());
		
		indiUser.setPassword("        ");
	    Set<ConstraintViolation<IndividualUser>> violationsBlank1 = validator.validate(indiUser);
	    assertEquals(1, violationsBlank1.size());
	    
	    indiUser.setPassword("");
	    Set<ConstraintViolation<IndividualUser>> violationsBlank2 = validator.validate(indiUser);
	    assertEquals(2, violationsBlank2.size());
	}
	
	@Test
	public void testMakeSureNamesValid() {
		// valid names
		indiUser.setName("Food Share Valid");
	    Set<ConstraintViolation<IndividualUser>> violationsNone = validator.validate(indiUser);
	    assertEquals(0, violationsNone.size());
	    
	    //invalid names
	    indiUser.setName("Assassin Tan Lee Sin");
	    Set<ConstraintViolation<IndividualUser>> violationsNoFunnyNames = validator.validate(indiUser);
	    assertEquals(1, violationsNoFunnyNames.size());
	    
	    indiUser.setName("short");
		Set<ConstraintViolation<IndividualUser>> violationSize1 = validator.validate(indiUser);
		assertEquals(1, violationSize1.size());
		
		indiUser.setName("VeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryVeryLongName");
		Set<ConstraintViolation<IndividualUser>> violationSize2 = validator.validate(indiUser);
		assertEquals(1, violationSize2.size());
		
		indiUser.setName("Sonic Tan 99");
		Set<ConstraintViolation<IndividualUser>> violationPattern = validator.validate(indiUser);
		assertEquals(1, violationPattern.size());
	    
	    indiUser.setName("      ");
	    Set<ConstraintViolation<IndividualUser>> violationsBlank1 = validator.validate(indiUser);
	    assertEquals(1, violationsBlank1.size());
	    
	    indiUser.setName("");
	    Set<ConstraintViolation<IndividualUser>> violationsBlank2 = validator.validate(indiUser);
	    assertEquals(2, violationsBlank2.size());
	}
	
	@Test
	public void testMakeSureNumbersValid() {
		
		//valid number
		indiUser.setMobileNo("+6581234567");
		Set<ConstraintViolation<IndividualUser>> violationNone = validator.validate(indiUser);
		assertEquals(0, violationNone.size());
		
		//invalid numbers
		indiUser.setMobileNo("6581234567");
		Set<ConstraintViolation<IndividualUser>> violationPattern1 = validator.validate(indiUser);
		assertEquals(1, violationPattern1.size());
		
		indiUser.setMobileNo("abc12345678");
		Set<ConstraintViolation<IndividualUser>> violationPattern2 = validator.validate(indiUser);
		assertEquals(1, violationPattern2.size());
		
		// the @Pattern prevents whitespaces and empty strings
		indiUser.setMobileNo("      ");
	    Set<ConstraintViolation<IndividualUser>> violationsBlank1 = validator.validate(indiUser);
	    assertEquals(1, violationsBlank1.size());
	    
	    indiUser.setMobileNo("");
	    Set<ConstraintViolation<IndividualUser>> violationsBlank2 = validator.validate(indiUser);
	    assertEquals(1, violationsBlank2.size());
		
	}
	
	@Test
	public void testMakeSureRewardPointsValid() {
		//valid rewardPts
		indiUser.setRewardPts(10000);
		Set<ConstraintViolation<IndividualUser>> violationNone = validator.validate(indiUser);
	    assertEquals(0, violationNone.size());
	    
	    //invalid rewardPts
	    indiUser.setRewardPts(-1);
		Set<ConstraintViolation<IndividualUser>> violationMin = validator.validate(indiUser);
	    assertEquals(1, violationMin.size());
	}
	
	
	// further tests for updating, retrieving, deleting if necessary
	
	

}
