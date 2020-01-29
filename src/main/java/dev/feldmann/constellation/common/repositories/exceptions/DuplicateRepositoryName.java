package dev.feldmann.constellation.common.repositories.exceptions;

import dev.feldmann.constellation.common.repositories.Repository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public class DuplicateRepositoryName extends RuntimeException {

    @Getter
    private Repository repository;

}
