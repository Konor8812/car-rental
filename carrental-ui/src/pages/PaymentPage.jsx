import React from "react";
import { useParams, useNavigate } from "react-router-dom";
import { api, PAYMENTS_URL } from "../api/api";

export default function PaymentPage() {
    const { paymentId } = useParams();
    const navigate = useNavigate();

    async function confirmPayment() {
        try {
            await api(PAYMENTS_URL, "/payments/complete", {
                method: "POST",
                body: { externalPaymentId: paymentId, isSuccessful: true },
            });
            alert("Payment confirmed!");
            navigate("/cars");
        } catch (err) {
            alert("Error confirming payment: " + err.message);
        }
    }

    return (
        <div className="bg-white rounded shadow p-6 max-w-md mx-auto text-center">
            <h3 className="text-xl font-semibold">Simulate Payment</h3>
            <p className="mt-2 text-gray-600">
                Simulating external processor for payment #{paymentId}
            </p>
            <button
                onClick={confirmPayment}
                className="mt-6 px-4 py-2 bg-indigo-600 text-white rounded"
            >
                Confirm Payment
            </button>
        </div>
    );
}
