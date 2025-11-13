import React, { useEffect, useState } from "react";
import { api, CORE_URL } from "../api/api";
import { useAuth } from "../auth/AuthContext.jsx";

export default function ReservationsList() {
    const { token } = useAuth();
    const [reservations, setReservations] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [releasing, setReleasing] = useState(null); // track which reservation is being released

    useEffect(() => {
        loadReservations();
    }, []);

    async function loadReservations() {
        setLoading(true);
        setError(null);
        try {
            const res = await api(CORE_URL, "/reservations", { token });
            // Assuming backend returns { reservations: [ { id, car, status, createdAt, validUntil } ] }
            setReservations(res.reservations || []);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }

    async function releaseReservation(reservationId) {
        setReleasing(reservationId);
        try {
            await api(CORE_URL, `/reservations/${reservationId}/release`, {
                method: "POST",
                token,
            });
            await loadReservations(); // refresh list
        } catch (err) {
            alert("Failed to release reservation: " + err.message);
        } finally {
            setReleasing(null);
        }
    }

    return (
        <div className="bg-white rounded shadow p-6">
            <h2 className="text-lg font-semibold">My Reservations</h2>
            {loading && <div className="mt-4">Loading...</div>}
            {error && <div className="mt-4 text-red-600">{error}</div>}

            {!loading && reservations.length === 0 && !error && (
                <div className="mt-4 text-gray-600">You have no reservations yet.</div>
            )}

            {!loading && reservations.length > 0 && (
                <div className="mt-4 overflow-x-auto">
                    <table className="min-w-full border-collapse border border-gray-200">
                        <thead>
                        <tr className="bg-gray-100">
                            <th className="border p-2 text-left">Car</th>
                            <th className="border p-2 text-left">Status</th>
                            <th className="border p-2 text-left">Created</th>
                            <th className="border p-2 text-left">Valid Until</th>
                            <th className="border p-2 text-left">Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        {reservations.map((r, i) => (
                            <tr key={i} className="hover:bg-gray-50">
                                <td className="border p-2">
                                    {r.car?.manufacturer} {r.car?.model} ({r.car?.year})
                                </td>
                                <td className="border p-2">{r.status}</td>
                                <td className="border p-2">
                                    {new Date(r.createdAt).toLocaleString()}
                                </td>
                                <td className="border p-2">
                                    {new Date(r.validUntil).toLocaleString()}
                                </td>
                                <td className="border p-2 text-right">
                                    {r.status === "awaits_payment" && (
                                        <button
                                            onClick={() => releaseReservation(r.id)}
                                            disabled={releasing === r.id}
                                            className={`px-3 py-1 rounded text-sm ${
                                                releasing === r.id
                                                    ? "bg-gray-300 text-gray-600 cursor-not-allowed"
                                                    : "bg-red-600 text-white hover:bg-red-700"
                                            }`}
                                        >
                                            {releasing === r.id ? "Releasing..." : "Release"}
                                        </button>
                                    )}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}
