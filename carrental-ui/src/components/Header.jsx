import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext.jsx";

export default function Header() {
    const { token, logout } = useAuth();
    const navigate = useNavigate();

    return (
        <header className="flex items-center justify-between p-4 border-b">
            {/* Left */}
            <Link to="/" className="text-2xl font-semibold">CarRental</Link>
            <nav className="flex items-center gap-6">

                <Link to="/reservations" className="text-lg hover:underline">
                    My reservations
                </Link>
            </nav>
            <nav className="flex items-center gap-6">
                <Link to="/cars" className="text-lg hover:underline">
                    Browse cars
                </Link>
            </nav>
                {/* Right */}
                {token ? (
                    <button
                        onClick={() => {
                            logout();
                            navigate("/auth");
                        }}
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
