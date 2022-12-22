package com.bootcamp.contracts;

import com.bootcamp.states.TokenState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;

import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

public class TokenContract implements Contract {
    public static String ID = "com.bootcamp.contracts.TokenContract";

    @Override
    public void verify(LedgerTransaction tx) throws IllegalArgumentException {
        if (tx.getInputStates().size() !=0){
            throw new IllegalArgumentException("Token Contract requires zero inputs in the transaction");
        }
        if (tx.getOutputStates().size() !=1){
            throw new IllegalArgumentException("Token Contract requires 1 outputs in the transaction");
        }
        if(tx.getCommands().size() !=1){
            throw new IllegalArgumentException("Token Contract requires only one command in the transaction");
        }
        if(!(tx.getOutput(0)instanceof TokenState)){
            throw new IllegalArgumentException("Token Contract requires the transaction output to be a token state");
        }
        TokenState tokenState = (TokenState) tx.getOutput(0);
        if(!(tokenState.getAmount()>0)){
            throw new IllegalArgumentException("Token Contract requires the transaction output to have positive amount");
        }
        if(!(tx.getCommand(0).getValue() instanceof TokenContract.Commands.Issue)){
            throw new IllegalArgumentException("Token Contract requires the transaction command to be an issue command");
        }
        if(!(tx.getCommand(0).getSigners().contains(tokenState.getIssuer().getOwningKey()))){
            throw new IllegalArgumentException("Token Contract requires the issuer to sign the transaction");
        }
    }


    public interface Commands extends CommandData {
        class Issue implements Commands { }
    }
}