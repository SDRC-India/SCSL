package org.sdrc.scsl.repository.springdatajpa;

import javax.persistence.LockModeType;

import org.sdrc.scsl.domain.MSTUser;
import org.sdrc.scsl.repository.MSTUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
/**
 * 
 * @since 1.0.0
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in) on 24-Apr-2017 7:18:45 pm
 */
public interface SpringDataMSTUserRepository extends MSTUserRepository, JpaRepository<MSTUser, Integer> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Transactional
	@Override
	@Query("select u from MSTUser u where u.userName = :username and u.password = :password and u.isLive = true")
	MSTUser findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
	
	@Override
	@Transactional
	@Modifying
	@Query("UPDATE MSTUser user SET user.password = :password WHERE user.userId = :userId")
	void updateByUserId(@Param("userId") Integer userId, @Param("password") String password);
	
	@Override
	@Query("SELECT max(usr.userId) FROM MSTUser usr")
	Integer findMaxUserId();
}
