import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext.jsx";

export default function Header() {
    const { token, logout } = useAuth();
    const navigate = useNavigate();

    return (
        <header className="flex items-center justify-between gap-4">
            <Link to="/" className="text-2xl font-semibold">CarRental</Link>
            <Link to="/reservations" className="text-2xl font-semibold">My reservations</Link>
            <Link to="/cars" className="px-3 py-1 rounded hover:bg-gray-100">Browse cars</Link>
            {token ? (
                <button
                    onClick={() => { logout(); navigate("/auth"); }}
                    className="px-3 py-1 bg-red-500 text-white rounded"
                >
                    Logout
                </button>
            ) : (
                <Link to="/auth" className="px-3 py-1 bg-blue-600 text-white rounded">
                    Login / Register
                </Link>
            )}
        </header>
    );
}
