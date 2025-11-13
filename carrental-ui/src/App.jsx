import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./auth/AuthContext.jsx";
import Home from "./pages/Home.jsx"
import AuthPage from "./auth/AuthPage.jsx"
import CarsList from "./pages/CarsList.jsx"
import CarDetail from "./pages/CarDetail.jsx"
import ReservePage from "./pages/ReservePage.jsx"
import PaymentPage from "./pages/PaymentPage.jsx"
import NotFound from "./components/NotFound.jsx"
import Header from "./components/Header.jsx"
import ReservationsList from "./pages/ReservationsList.jsx";

export default function App() {
    return (
        <BrowserRouter>
            <AuthProvider>
                <div className="min-h-screen bg-gray-50">
                    <div className="max-w-5xl mx-auto p-6">
                        <Header />
                        <div className="mt-6">
                            <Routes>
                                <Route path="/" element={<Home />} />
                                <Route path="/auth" element={<AuthPage />} />
                                <Route path="/cars" element={<CarsList />} />
                                <Route path="/cars/:carId" element={<CarDetail />} />
                                <Route path="/cars/:carId/reserve" element={<ReservePage />} />
                                <Route path="/payments/:paymentId" element={<PaymentPage />} />
                                <Route path="/reservations" element={<ReservationsList />} />
                                <Route path="*" element={<NotFound />} />
                            </Routes>
                        </div>
                    </div>
                </div>
            </AuthProvider>
        </BrowserRouter>
    );
}
