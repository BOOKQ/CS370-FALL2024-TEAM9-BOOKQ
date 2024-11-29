document.getElementById("submit-btn").addEventListener("click", async function () {
    const form = document.getElementById("quiz-form");
    const formData = new FormData(form);

    // Convert FormData to a JSON object
    const data = {};
    formData.forEach((value, key) => {
        data[key] = value;
    });

    try {
        // Send the data to the server using fetch
        const response = await fetch("/quiz", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });

        // Process the server response
        if (response.ok) {
            const result = await response.json();
            document.getElementById("results").innerHTML = `
                <h2>Quiz Results</h2>
                <p>${result.message}</p>
                <p>Your score: ${result.score}/10</p>
            `;
        } else {
            document.getElementById("results").innerHTML = `<p>Error: Could not submit quiz.</p>`;
        }
    } catch (error) {
        console.error("Error submitting quiz:", error);
        document.getElementById("results").innerHTML = `<p>Error: Could not submit quiz.</p>`;
    }
});
