// src/pages/Main.jsx
import { useNavigate } from "react-router-dom";

export default function Main() {
    const navigate = useNavigate();

    return (
        <div style={{ textAlign: "center", marginTop: "100px" }}>
            <h1>Welcome to the Car Rental Service 🚗</h1>
            <div style={{ marginTop: "20px" }}>
                <button onClick={() => navigate("/login")} style={{ margin: "10px", padding: "10px 20px" }}>
                    Login
                </button>
                <button onClick={() => navigate("/register")} style={{ margin: "10px", padding: "10px 20px" }}>
                    Register
                </button>
                <button onClick={() => navigate("/logout")} style={{ margin: "10px", padding: "10px 20px" }}>
                    Logout
                </button>
            </div>
        </div>
    );
}
