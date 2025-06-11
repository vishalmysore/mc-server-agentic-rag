package org.example;

import com.t4a.annotations.Action;
import com.t4a.annotations.Agent;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * LiconlinLawyer is a virtual legal assistant agent that compares, reviews, and extracts key legal information.
 */
@Service
@Log
@Agent(groupName = "legalTools", groupDescription = "AI tools for reviewing legal clauses and contracts")
public class LiconlinLawyerService {

    public LiconlinLawyerService() {
        log.info("LiconlinLawyerService initialized");
    }

    @Action(description = "Compare clauses in two contracts and highlight differences")
    public String compareClauses(String clause1, String clause2) {
        if (clause1.equalsIgnoreCase(clause2)) {
            return "Both clauses are identical.";
        }

        StringBuilder result = new StringBuilder("Differences detected:\n");

        if (clause1.contains("indemnity") && !clause2.contains("indemnity")) {
            result.append("- Clause 1 contains indemnity language missing in Clause 2.\n");
        } else if (!clause1.contains("indemnity") && clause2.contains("indemnity")) {
            result.append("- Clause 2 contains indemnity language missing in Clause 1.\n");
        }

        if (clause1.contains("liability") && !clause2.contains("liability")) {
            result.append("- Clause 1 refers to liability terms not mentioned in Clause 2.\n");
        }

        return result.toString();
    }

    @Action(description = "Check for missing risk terms like indemnity, liability, and termination")
    public String detectMissingRiskTerms(String clause) {
        List<String> riskTerms = Arrays.asList("indemnity", "liability", "termination", "jurisdiction", "governing law");
        StringBuilder missing = new StringBuilder("Missing key legal terms:\n");

        for (String term : riskTerms) {
            if (!clause.toLowerCase().contains(term)) {
                missing.append("- ").append(term).append("\n");
            }
        }

        return missing.toString();
    }

    @Action(description = "Extract parties involved in a clause using pattern heuristics")
    public String extractParties(String clause) {
        // Very naive heuristic, should be improved with NLP
        if (clause.toLowerCase().contains("party a") && clause.toLowerCase().contains("party b")) {
            return "Detected Parties: Party A, Party B";
        }

        return "Unable to confidently detect named parties. Please provide more context.";
    }

    @Action(description = "Summarize a legal clause in plain English")
    public String summarizeClause(String clause) {
        if (clause.toLowerCase().contains("termination")) {
            return "This clause describes the conditions under which the contract can be terminated.";
        }
        if (clause.toLowerCase().contains("indemnify")) {
            return "This clause defines indemnification obligations.";
        }
        return "Summary: General legal clause. Please review manually.";
    }

    @Action(description = "Check if a clause complies with standard legal best practices")
    public String checkCompliance(String clause) {
        boolean hasDates = clause.toLowerCase().contains("effective date");
        boolean hasJurisdiction = clause.toLowerCase().contains("jurisdiction");
        boolean hasGoverningLaw = clause.toLowerCase().contains("governing law");

        if (hasDates && hasJurisdiction && hasGoverningLaw) {
            return "Clause aligns with standard legal compliance checks.";
        } else {
            return "Clause may be non-compliant or missing essential legal attributes like dates, jurisdiction, or governing law.";
        }
    }
}
