## DevOps Engineer Technical Assessment

### Introduction

This technical assessment is designed to evaluate your ability to design, automate, and operate a simple application in a way that reflects real-world delivery and operational practices.

The objective is not to build a complex system or demonstrate deep expertise in any specific tool. Instead, this exercise evaluates your ability to take a working application and structure it so that it can be consistently built, configured, deployed, and executed in a reliable and repeatable manner.

We are particularly interested in how you think about system reliability, environment configuration, automation, and operational clarity. The way you structure your solution, explain your decisions, and balance simplicity with completeness is more important than the breadth of tooling you introduce.

Clarity, structure, and sound engineering judgment are valued more highly than technical complexity.

---

### Scope and Time Expectations

This assessment must be completed within a 48-hour window. The expected development effort is approximately 4 – 8 hours.

The goal is not to encourage overengineering. A focused, well-reasoned implementation is preferred over a complex but fragile or poorly explained solution.

This project is not proprietary work and will not be used commercially. All submitted work remains yours and will be evaluated solely for hiring purposes.

---

### Submission Requirements

The completed project must be hosted in a GitHub repository and shared with m-segreti.

The repository must be complete, self-contained, and runnable locally. We must be able to clone the repository and execute the system based solely on the documentation you provide.

All design and tooling decisions must be documented and justified.

If the project cannot be executed locally using the instructions in the repository, it will not be evaluated.

---

### Project Objective

You are provided with this [repository](https://github.com/m-segreti/ar-doe-assessment), a minimal application consisting of a backend service.

Your task is to take this application and prepare it for consistent execution across environments. The application should be capable of being built, configured, and run in a way that does not rely on manual intervention or machine-specific setup.

The system should be containerized such that it can be executed in a predictable and isolated manner. Configuration should be externalized so that the application can run in different environments without requiring code changes.

You should introduce an automated build and delivery process that ensures the application can be reliably packaged and prepared for execution. This process should demonstrate how changes to the application move from source code to a runnable artifact.

The system should include a clear method for orchestrating its runtime dependencies. Whether you choose to run the system using container composition or a lightweight orchestration approach is left to your discretion, but the result should be a system that can be started and stopped in a controlled and repeatable way.

Basic operational considerations should be reflected in your implementation. The application should expose a clear indication of its health and should produce meaningful output that would allow an operator to understand its state during execution.

The intent of this exercise is to demonstrate your ability to think about how software is delivered and operated, not just how it is written.

---

### Technology Expectations

You may use any tools or frameworks you consider appropriate.

The emphasis is not on selecting specific technologies, but on how effectively you apply them to create a clear, maintainable, and reproducible system.

---

### System Design Expectations

While the system itself is intentionally small, the way it is structured should reflect sound operational practices.

The solution should demonstrate a clear separation between build, configuration, and runtime concerns. It should be possible to understand how the system is constructed, how it is configured, and how it is executed without ambiguity.

We are interested in the reasoning behind your choices. Tradeoffs, simplifications, and intentional omissions are all acceptable if they are clearly explained.

---

### Required Documentation

A README.md must be included in the repository and should function as internal engineering documentation for the system.

The documentation should include a high-level description of how the system is built and executed, how configuration is managed, and how the different components interact at runtime.

You should explain the structure of your solution, the purpose of the tools you selected, and the reasoning behind those decisions.

You should also describe any tradeoffs you made, limitations of the current implementation, and how the system could be extended or improved if more time were available.

Clear instructions must be provided explaining how to build and run the system locally, including any prerequisites or required environment configuration.

---

### Communication and Support

This assessment is not intended to be a closed exercise.

In real-world environments, DevOps work requires constant communication, clarification, and iteration. If you encounter ambiguity or need to make assumptions, you are encouraged to document those decisions or reach out for clarification.

You may contact me directly via email at [msegreti@alpharecon.com](mailto:msegreti@alpharecon.com).

Asking thoughtful questions and making intentional decisions is considered a strength.
