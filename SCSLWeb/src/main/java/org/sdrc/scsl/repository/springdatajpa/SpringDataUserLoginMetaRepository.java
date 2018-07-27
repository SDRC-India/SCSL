package org.sdrc.scsl.repository.springdatajpa;

import java.sql.Timestamp;

import org.sdrc.scsl.domain.UserLoginMeta;
import org.sdrc.scsl.repository.UserLoginMetaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
* @author Sarita Panigrahi(sarita@sdrc.co.in)
* created on: 20-Sep-2017
*/
public interface SpringDataUserLoginMetaRepository extends UserLoginMetaRepository, JpaRepository<UserLoginMeta, Integer> {


	@Override
	@Modifying 
	@Transactional
	@Query("UPDATE UserLoginMeta logInMeta SET logInMeta.loggedOutDateTime = :loggedOutDateTime , "
			+ "logInMeta.isLoggedIn =FALSE WHERE logInMeta.userLogInMetaId = :userLogInMetaId ")
	void updateStatus(@Param("loggedOutDateTime")Timestamp loggedOutDateTime, @Param("userLogInMetaId")long userLogInMetaId);
	
	@Override
	@Modifying 
	@Transactional
	@Query("UPDATE"
			+ " UserLoginMeta logInMeta SET "
			+ "logInMeta.loggedOutDateTime = :loggedOutDateTime , "
			+ "logInMeta.isLoggedIn =FALSE ")
	void updateStatusForAll(@Param("loggedOutDateTime")Timestamp loggedOutDateTime);
}
