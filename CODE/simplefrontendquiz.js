const questions = [
    {question: "What is your favorite genre?", options: ["Fiction", "Non-fiction", "Fantasy", "Mystery", "Romance"], answer: null},
    {question: "What is your least favorite genre?", options: ["Fiction", "Non-fiction", "Fantasy", "Mystery", "Romance"], answer: null},
    {question: "How long do you like a book to be?", options: ["Short", "Medium", "Long"], answer: null}
];

let currentQuestionIndex = 0;

const questionElement = document.getElementById("question");
const optionsElement = document.getElementById("options");
const submitButton = document.getElementById("submitButton");

function displayQuestion() {
    const question = questions[currentQuestionIndex];
    questionElement.textContent = question.question;
    optionsElement.innerHTML = "";
    question.options.forEach((option, index) => {
        const button = document.createElement("button");
        button.textContent = option;
        button.classList.add("button");
        button.onclick = () => selectOption(index);
        optionsElement.appendChild(button);
    });
    submitButton.style.display = "none";
}

function selectOption(optionIndex) {
    questions[currentQuestionIndex].answer = optionIndex;
    submitButton.style.display = "inline-block";
}

submitButton.onclick = () => {
    const formData = new FormData();
    formData.append("favorite_genre", questions[0].options[questions[0].answer]);
    formData.append("least_favorite_genre", questions[1].options[questions[1].answer]);
    formData.append("book_length_preference", questions[2].options[questions[2].answer]);
    formData.append("user_id", 1);

    fetch("/submitQuiz", {method: "POST", body: formData})
    .then(response => response.text())
    .then(data => { document.getElementById("quiz").innerHTML = `<h2>${data}</h2>`; });
};

displayQuestion();