package ru.practicum.repository.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.compilation.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    Page<Compilation> findAllByPinnedOrderByIdAsc(Boolean pinned, Pageable pageable);
}
