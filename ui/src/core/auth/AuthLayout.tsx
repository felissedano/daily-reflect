import React from 'react'
import {Container} from "@radix-ui/themes";

interface AuthLayoutProp {
    children: React.ReactNode
}
const AuthLayout: React.FC<AuthLayoutProp> = ({ children }: {children: React.ReactNode }) => {
    return (
        <>
            <Container>

            <h1>Daily Reflect</h1>
            <main>
                { children }
            </main>
            <footer>
                <h4>Footer</h4>
            </footer>
            </Container>
        </>
    )
}

export default AuthLayout
