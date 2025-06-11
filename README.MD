## Agentic Rag

Agentic RAG is a next-generation AI architecture that combines the precision of structured agent-based reasoning with the power of retrieval-augmented generation. Unlike traditional RAG pipelines that blindly retrieve documents for every query, Agentic RAG introduces intelligent agentic filters that first analyze, decompose, and decide whether a query requires retrieval at all. This approach enables deterministic responses for rule-based or well-defined tasks, reducing hallucinations, lowering compute costs, and improving latency—while still falling back to RAG or LLMs when novel or open-ended questions arise. It’s not about replacing RAG, but making it smarter, more targeted, and truly enterprise-ready  

**A2AJava is the Java implementation** of the Google A2A protocol you can check the project [here](https://github.com/vishalmysore/a2ajava) . 

```  
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

```

to test this use 

detectMissingRiskTerms 

```bash
curl -v \
-H "Content-Type: application/json" \
-d '{
"jsonrpc": "2.0",
"method": "tasks/send",
"params": {
"id": "999789",
"sessionId": "sdf44555",
"message": {
"role": "user",
"parts": [
{
"type": "text",
"text": "Please analyze the following clause for any missing risk terms: The parties agree to indemnify each other for any losses incurred during the execution of this agreement.",
"metadata": null
}
]
}
}
}' http://localhost:7860

```

summarizeClause

```bash
 curl -v \
-H "Content-Type: application/json" \
-d '{
"jsonrpc": "2.0",
"method": "tasks/send",
"params": {
"id": "999789",
"sessionId": "sdf44555",
"message": {
"role": "user",
"parts": [
{
"type": "text",
"text": "Please summarize the following clause: In the event of a breach of contract, the non-breaching party shall be entitled to seek damages in accordance with applicable law.",
"metadata": null
}
]
}
}
}' http://localhost:7860
```

checkCompliance  

```bash
curl -v \
-H "Content-Type: application/json" \
-d '{
"jsonrpc": "2.0",
"method": "tasks/send",
"params": {
"id": "999789",
"sessionId": "sdf44555",
"message": {
"role": "user",
"parts": [
{
"type": "text",
"text": "Please check if the following clause is in compliance: Ensure all safety protocols are followed during the operation of machinery.",
"metadata": null
}
]
}
}
}' http://localhost:7860
```



compareClauses, Value:

```bash
curl  \
-H "Content-Type: application/json" \
-d '{
"jsonrpc": "2.0",
"method": "tools/call",
"params": {
"name": "compareClauses",
"arguments": {
"provideAllValuesInPlainEnglish": {
"value": "Please compare the two clauses: The quick brown fox jumps over the lazy dog. and A swift auburn fox leaps over a sleepy hound. What are the similarities and differences between them?"
}
}
},
"id": 25
}' http://localhost:7860
```

detectMissingRiskTerms,  

```bash
curl  \
-H "Content-Type: application/json" \
-d '{
"jsonrpc": "2.0",
"method": "tools/call",
"params": {
"name": "detectMissingRiskTerms",
"arguments": {
"provideAllValuesInPlainEnglish": {
"value": "Please analyze the following clause for any missing risk terms: The parties agree to indemnify each other for any losses incurred during the execution of this agreement."
}
}
},
"id": 25
}' http://localhost:7860
```
Key: MCP_extractParties, Value:
```bash

curl  \
-H "Content-Type: application/json" \
-d '{
"jsonrpc": "2.0",
"method": "tools/call",
"params": {
"name": "extractParties",
"arguments": {
"provideAllValuesInPlainEnglish": {
"value": "Please extract the parties involved from the following clause: John Doe and Jane Smith entered into an agreement on March 1, 2023."
}
}
},
"id": 25
}' http://localhost:7860
```
Key: A2A_extractParties, Value: 
```bash
 curl -v \
-H "Content-Type: application/json" \
-d '{
"jsonrpc": "2.0",
"method": "tasks/send",
"params": {
"id": "999789",
"sessionId": "sdf44555",
"message": {
"role": "user",
"parts": [
{
"type": "text",
"text": "Please extract the parties involved from the following clause: John Doe and Jane Smith entered into an agreement on March 1, 2023.",
"metadata": null
}
]
}
}
}' http://localhost:7860
```
Key: A2A_compareClauses, Value: 
```bash
 curl -v \
-H "Content-Type: application/json" \
-d '{
"jsonrpc": "2.0",
"method": "tasks/send",
"params": {
"id": "999789",
"sessionId": "sdf44555",
"message": {
"role": "user",
"parts": [
{
"type": "text",
"text": "Please compare the two clauses: The quick brown fox jumps over the lazy dog. and A swift auburn fox leaps over a sleepy hound. What are the similarities and differences between them?",
"metadata": null
}
]
}
}
}' http://localhost:7860
```
Key: MCP_checkCompliance, Value: 
```bash
 curl  \
-H "Content-Type: application/json" \
-d '{
"jsonrpc": "2.0",
"method": "tools/call",
"params": {
"name": "checkCompliance",
"arguments": {
"provideAllValuesInPlainEnglish": {
"value": "Please check if the following clause is in compliance: Ensure all safety protocols are followed during the operation of machinery."
}
}
},
"id": 25
}' http://localhost:7860
```

Key: MCP_summarizeClause, Value:

```bash
 curl  \
-H "Content-Type: application/json" \
-d '{
"jsonrpc": "2.0",
"method": "tools/call",
"params": {
"name": "summarizeClause",
"arguments": {
"provideAllValuesInPlainEnglish": {
"value": "Please summarize the following clause: In the event of a breach of contract, the non-breaching party shall be entitled to seek damages in accordance with applicable law."
}
}
},
"id": 25
}' http://localhost:7860
```


🔧 Agentic RAG Architecture

```scss

User Query
↓
Agentic Router
├──→ Structured Agent Tool (Java, MCP, A2A)
├──→ Direct Response
├──→ Agentic Workflow (e.g., LegalTool → 3 subtools)
└──→ RAG (if needed) → LLM
```

✨ Example Use Case: Legal Agent
```css

Input: "Is this clause compliant with GDPR and safe for indemnity?"
↓

LegalAgent:
- checkCompliance()
- detectMissingRiskTerms()
- compareClauses()

↓

Output: Direct + Structured
Only if:
```

"What did the court say about this clause in the EU case law?"  
→ Then → Vector Search → RAG → LLM Summarization