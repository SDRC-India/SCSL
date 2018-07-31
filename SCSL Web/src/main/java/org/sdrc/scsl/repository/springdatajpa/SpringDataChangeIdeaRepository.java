/**
 * 
 */
package org.sdrc.scsl.repository.springdatajpa;

import org.sdrc.scsl.domain.ChangeIdea;
import org.sdrc.scsl.repository.ChangeIdeaRepository;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
@RepositoryDefinition(domainClass=ChangeIdea.class,idClass=Integer.class)
public interface SpringDataChangeIdeaRepository extends ChangeIdeaRepository {

}
