import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import {api, AUTH_URL} from "../api/api";
import {useAuth} from "./AuthContext.jsx";

export default function AuthPage() {
    const {saveToken} = useAuth();
    const [mode, setMode] = useState("login");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [username, setUsername] = useState("");
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    async function submit(e) {
        e.preventDefault();
        setError(null);
        try {
            const endpoint = mode === "login" ? "/auth/login" : "/auth/register";
            const hashedPassword = await hashPassword(password);
            const body = mode === "login" ? {email, password: hashedPassword} : {email, password: hashedPassword, username};
            const res = await api(AUTH_URL, endpoint, {method: "POST", body});
            saveToken(res.token);
            navigate("/cars");
        } catch (err) {
            setError(err.message);
        }
    }

    async function hashPassword(password) {
        const encoder = new TextEncoder();
        const data = encoder.encode(password);
        const hashBuffer = await crypto.subtle.digest("SHA-256", data);
        const hashArray = Array.from(new Uint8Array(hashBuffer));
        return hashArray.map((b) => b.toString(16).padStart(2, "0")).join("");
    }

    return (
        <div className="max-w-md mx-auto bg-white rounded shadow p-6">
            <h2 className="text-lg font-semibold">{mode === "login" ? "Login" : "Register"}</h2>
            <form onSubmit={submit} className="mt-4 space-y-3">
                <input value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email"
                       className="w-full p-2 border rounded"/>
                <input value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password"
                       type="password" className="w-full p-2 border rounded"/>
                {mode === "register" && (
                    <input value={username} onChange={(e) => setUsername(e.target.value)} placeholder="Username"
                           className="w-full p-2 border rounded"/>
                )}
                {error && <div className="text-red-600">{error}</div>}
                <div className="flex items-center gap-2">
                    <button className="px-3 py-2 bg-indigo-600 text-white rounded" type="submit">
                        {mode === "login" ? "Login" : "Register"}
                    </button>
                    <button type="button" onClick={() => setMode(mode === "login" ? "register" : "login")}
                            className="text-sm underline">
                        Switch to {mode === "login" ? "Register" : "Login"}
                    </button>
                </div>
            </form>
        </div>
    );
}
