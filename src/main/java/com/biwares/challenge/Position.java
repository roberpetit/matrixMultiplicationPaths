package com.biwares.challenge;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
class Position implements Serializable {
    int row;
    int column;
}
