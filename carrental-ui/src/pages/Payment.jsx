// src/Payment.jsx
import { useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";

export default function Payment() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const [name, setName] = useState("");
    const [card, setCard] = useState("");
    const [paid, setPaid] = useState(false);
    const [code, setCode] = useState("");

    const paymentLink = searchParams.get("link");

    const handlePay = () => {
        // mock success
        const generatedCode = Math.floor(10000000 + Math.random() * 90000000);
        setCode(generatedCode);
        setPaid(true);
    };

    if (paid) {
        return (
            <div style={{ padding: "20px" }}>
                <h2>Payment Successful 🎉</h2>
                <p>Thank you, {name}!</p>
                <p>Your rental unlock code is:</p>
                <h1 style={{ color: "green" }}>{code}</h1>
                <button onClick={() => navigate("/")}>Back to Cars</button>
            </div>
        );
    }

    return (
        <div style={{ padding: "20px", maxWidth: "400px" }}>
            <h2>Complete Payment</h2>
            <p>Transaction: {paymentLink}</p>

            <label>Name on Card:</label>
            <input
                style={{ display: "block", marginBottom: "10px", width: "100%" }}
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="John Doe"
            />

            <label>Card Number:</label>
            <input
                style={{ display: "block", marginBottom: "20px", width: "100%" }}
                value={card}
                onChange={(e) => setCard(e.target.value)}
                placeholder="1234 5678 9012 3456"
            />

            <button onClick={handlePay} disabled={!name || !card}>
                Pay Now
            </button>
        </div>
    );
}
