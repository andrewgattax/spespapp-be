package it.trinex.spespappbe.repo;

import it.trinex.spespappbe.model.SpespItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface SpespItemRepo extends JpaRepository<SpespItem, Long> {

    List<SpespItem> findAllByChecked(boolean checked);

    @Query("SELECT s FROM SpespItem s WHERE s.checked = true AND s.updatedAt >= :timeAfter")
    List<SpespItem> findAllRecentsBought(Instant daysAgoInstant);

}
