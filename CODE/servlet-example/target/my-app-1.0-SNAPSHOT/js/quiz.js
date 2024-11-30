document.addEventListener("DOMContentLoaded", async function () {
    const quizForm = document.getElementById("quiz-form");
    const submitBtn = document.getElementById("submit-btn");
    const resultsDiv = document.getElementById("results");

    // Fetch a random quiz from the server
    async function fetchQuiz() {
        try {
            const response = await fetch("/quiz", { method: "GET" });
            if (!response.ok) {
                throw new Error("Failed to fetch quiz");
            }
            const data = await response.json();
            displayQuiz(data.quiz);
        } catch (error) {
            console.error("Error fetching quiz:", error);
            resultsDiv.innerHTML = "<p>Error loading quiz. Please try again later.</p>";
        }
    }

    // Dynamically display the quiz questions
    function displayQuiz(questions) {
        quizForm.innerHTML = ""; // Clear existing content
        questions.forEach((question, index) => {
            const questionDiv = document.createElement("div");
            questionDiv.classList.add("question");

            const label = document.createElement("label");
            label.textContent = `${index + 1}. ${question}`;
            questionDiv.appendChild(label);

            const yesOption = document.createElement("input");
            yesOption.type = "radio";
            yesOption.name = `q${index + 1}`;
            yesOption.value = "yes";
            questionDiv.appendChild(yesOption);
            questionDiv.appendChild(document.createTextNode(" Yes "));

            const noOption = document.createElement("input");
            noOption.type = "radio";
            noOption.name = `q${index + 1}`;
            noOption.value = "no";
            questionDiv.appendChild(noOption);
            questionDiv.appendChild(document.createTextNode(" No "));

            quizForm.appendChild(questionDiv);
        });

        submitBtn.style.display = "block"; // Show the submit button
    }

    // Submit quiz answers to the server
    async function submitQuiz() {
        const formData = new FormData(quizForm);
        const data = {};
        formData.forEach((value, key) => {
            data[key] = value;
        });

        try {
            const response = await fetch("/quiz", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data),
            });

            if (!response.ok) {
                throw new Error("Error submitting quiz");
            }

            const result = await response.json();
            displayResults(result);
        } catch (error) {
            console.error("Error submitting quiz:", error);
            resultsDiv.innerHTML = "<p>There was an error submitting your quiz. Please try again.</p>";
        }
    }

    // Display quiz results
    function displayResults(result) {
        resultsDiv.innerHTML = `
            <h2>Quiz Results</h2>
            <p>${result.message}</p>
            <p>Your score: ${result.score}/10</p>
            <p>Recommended Genre: <strong>${result.recommendedGenre}</strong></p>
        `;
    }

    // Event listener for form submission
    submitBtn.addEventListener("click", submitQuiz);

    // Load the quiz on page load
    fetchQuiz();
});
