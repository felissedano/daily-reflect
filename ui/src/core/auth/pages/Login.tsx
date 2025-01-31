import {FaceIcon} from "@radix-ui/react-icons"
import {Button, Container, Text} from "@radix-ui/themes";
import AuthLayout from "../AuthLayout.tsx";
import React, {FormEvent} from "react";
import {useTranslation} from "react-i18next";

const Login: React.FC = () => {

    const { t } = useTranslation();

    const onSubmit =
        (event: FormEvent<HTMLFormElement>) => {
            event.preventDefault();
            console.log("Login");
        }

    return (
        <>
            <AuthLayout>

                    <h2>Please Login to access your journals</h2>
                    <form onSubmit={onSubmit}>
                        <label><Text>Email</Text></label>
                        <br/>
                        <input type={"email"}/>
                        <br/>
                        <Button type={"submit"}>{ t('login')}</Button>
                    </form>

            </AuthLayout>
        </>
    )
}

export default Login
