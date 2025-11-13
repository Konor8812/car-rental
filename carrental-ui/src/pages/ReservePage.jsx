import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { api, CORE_URL } from "../api/api";
import { useAuth } from "../auth/AuthContext.jsx";

export default function ReservePage() {
    const { token } = useAuth();
    const { carId } = useParams();
    const navigate = useNavigate();

    const [rate, setRate] = useState(null);
    const [numOfDays, setNumOfDays] = useState(1);
    const [total, setTotal] = useState(0);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => { loadRate(); }, [carId]);

    async function loadRate() {
        setLoading(true);
        try {
            const res = await api(CORE_URL, `/cars/${carId}/rate`, { method: "POST", token });
            setRate(res.rate);
            setTotal(res.rate * numOfDays);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }

    function handleDaysChange(e) {
        const days = Math.max(1, Number(e.target.value));
        setNumOfDays(days);
        if (rate) setTotal(rate * days);
    }

    async function confirmReservation() {
        try {
            const res = await api(CORE_URL, `/reservations`, {
                method: "POST",
                token,
                body: { carId, numOfDays, rate, total },
            });
            // Expecting res.paymentLink like "payments/{payment_id(uuid)}"
            const paymentIdMatch = res.paymentLink.match(
                /([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})/
            );

            const paymentId = paymentIdMatch ? paymentIdMatch[1] : null;
            navigate(`/payments/${paymentId}`);
        } catch (err) {
            setError(err.message);
        }
    }

    if (loading) return <div className="p-6 bg-white rounded shadow">Loading rate...</div>;
    if (error) return <div className="p-6 bg-white rounded shadow text-red-600">{error}</div>;
    if (!rate) return <div className="p-6 bg-white rounded shadow">Rate not available</div>;

    return (
        <div className="bg-white rounded shadow p-6 max-w-md mx-auto">
            <h3 className="text-xl font-semibold">Reserve Car</h3>
            <div className="mt-4">
                <div>Daily rate: <strong>{rate}</strong></div>
                <div className="mt-2">
                    <label>Number of days:</label>
                    <input
                        type="number"
                        min="1"
                        value={numOfDays}
                        onChange={handleDaysChange}
                        className="w-24 ml-2 p-2 border rounded"
                    />
                </div>
                <div className="mt-3">Total: <strong>{total.toFixed(2)}</strong></div>
                <button
                    onClick={confirmReservation}
                    className="mt-4 px-4 py-2 bg-green-600 text-white rounded"
                >
                    Confirm Reservation
                </button>
            </div>
        </div>
    );
}
