/**
 * 
 */
package org.sdrc.scsl.repository;

import java.util.List;

import org.sdrc.scsl.domain.SYSConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public interface SYSConfigurationRepository {

	@Transactional
	SYSConfiguration save(SYSConfiguration sysConfig);

	List<SYSConfiguration> findAll();

}
