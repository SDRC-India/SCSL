/**
 * 
 */
package org.sdrc.scsl.repository;

import java.util.List;

import org.sdrc.scsl.domain.ChangeIdea;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
public interface ChangeIdeaRepository {

	List<ChangeIdea> findAll();

	@Transactional
	ChangeIdea save(ChangeIdea changeIdeaDomain);

	ChangeIdea findByDescriptionIgnoreCaseAndIndicatorIndicatorId(
			String changeIdea, int indicatorId);

}
