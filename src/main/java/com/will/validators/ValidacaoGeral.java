package com.will.validators;

import org.springframework.stereotype.Component;

@Component
public class ValidacaoGeral {

    public void validateIdZeroOrNull(long id) throws Exception {
        if(id != 0 )
            throw new Exception("ID deve ser nulo ou zero");
    }
}
