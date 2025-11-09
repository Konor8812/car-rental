// src/pages/Register.jsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { registerUser, saveToken } from "../api";

export default function Register() {
    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const token = await registerUser(email, password, username);
            saveToken(token);
            navigate("/cars");
        } catch {
            setError("Registration failed");
        }
    };

    return (
        <div style={{ textAlign: "center", marginTop: "100px" }}>
            <h2>Register</h2>
            <form onSubmit={handleRegister} style={{ display: "inline-block", textAlign: "left" }}>
                <div style={{ margin: "10px 0" }}>
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div style={{ margin: "10px 0" }}>
                    <input
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div style={{ margin: "10px 0" }}>
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Register</button>
            </form>
            {error && <p style={{ color: "red" }}>{error}</p>}
        </div>
    );
}
