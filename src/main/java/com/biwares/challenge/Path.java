package com.biwares.challenge;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Camino
 * Lista ordenada a recorrer
 */
@Data
@AllArgsConstructor
class Path {
    List<Position> positions;

}
