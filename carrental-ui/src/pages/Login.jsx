// src/pages/Login.jsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginUser, saveToken } from "../api";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const token = await loginUser(email, password);
            saveToken(token);
            navigate("/cars");
        } catch {
            setError("Invalid credentials");
        }
    };

    return (
        <div style={{ textAlign: "center", marginTop: "100px" }}>
            <h2>Login</h2>
            <form onSubmit={handleLogin} style={{ display: "inline-block", textAlign: "left" }}>
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
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Login</button>
            </form>
            {error && <p style={{ color: "red" }}>{error}</p>}
        </div>
    );
}
