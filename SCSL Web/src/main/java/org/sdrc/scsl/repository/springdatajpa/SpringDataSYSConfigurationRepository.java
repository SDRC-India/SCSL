/**
 * 
 */
package org.sdrc.scsl.repository.springdatajpa;

import org.sdrc.scsl.domain.SYSConfiguration;
import org.sdrc.scsl.repository.SYSConfigurationRepository;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
@RepositoryDefinition(domainClass = SYSConfiguration.class, idClass = Integer.class)
public interface SpringDataSYSConfigurationRepository extends
		SYSConfigurationRepository {

}
