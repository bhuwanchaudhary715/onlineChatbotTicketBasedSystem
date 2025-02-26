// Function to add a message to the chat window
function addMessage(sender, message) {
    const chatWindow = document.querySelector('.chat-window');
    const messageDiv = document.createElement('div');
    messageDiv.classList.add('message', sender);
    
    let avatar = sender === 'bot' ? '🤖' : '🧑';
    messageDiv.innerHTML = `<div class="avatar">${avatar}</div><div class="text">${message}</div>`;
    chatWindow.appendChild(messageDiv);

    // Scroll to bottom of chat window
    chatWindow.scrollTop = chatWindow.scrollHeight;
}

// Function to fetch response from backend
async function fetchBotResponse(userMessage) {
    try {
        const response = await fetch('/api/museum/dialogflow/webhook', { // Update URL as needed
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                queryResult: {
                    queryText: userMessage
                }
            }),
        });

        if (!response.ok) {
            throw new Error(`Server error: ${response.status}`);
        }

        const data = await response.json();
        return data.fulfillmentText || "Sorry, I couldn't process that.";
    } catch (error) {
        console.error('Error fetching bot response:', error);
        return "I'm having trouble connecting to the server. Please try again later.";
    }
}

// Function to handle user's question and show bot's response
async function handleQuestion(question) {
    addMessage('user', question);
    const botResponse = await fetchBotResponse(question);
    addMessage('bot', botResponse);
}

// Add event listeners to predefined question buttons
document.querySelectorAll('.question-btn').forEach(button => {
    button.addEventListener('click', function () {
        const question = this.innerText;
        handleQuestion(question);
    });
});

// Handle the user's input when they type a message and click send
document.getElementById('sendBtn').addEventListener('click', function () {
    const userInput = document.getElementById('userInput').value.trim();
    if (userInput) {
        handleQuestion(userInput);
        document.getElementById('userInput').value = ''; // Clear input
    }
});

// Enable pressing "Enter" to send message
document.getElementById('userInput').addEventListener('keypress', function (event) {
    if (event.key === 'Enter') {
        event.preventDefault(); // Prevent form submission
        document.getElementById('sendBtn').click(); // Trigger send button
    }
});
