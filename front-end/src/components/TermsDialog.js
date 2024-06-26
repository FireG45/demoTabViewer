import React, {useEffect, useState} from 'react';
import {Button, Dialog, DialogActions, DialogContent, DialogTitle} from '@mui/material';

const TermsDialog = () => {
    const [open, setOpen] = useState(false);

    // Проверка cookies["terms"] при монтировании компонента
    useEffect(() => {
        const hasAgreed = localStorage.getItem('terms') === 'true';
        if (!hasAgreed) {
            setOpen(true);
        }
    }, []);

    // Сохранение согласия в cookies
    const handleAgree = () => {
        localStorage.setItem('terms', 'true');
        setOpen(false);
    };

    return (
        <Dialog open={open}>
            <DialogTitle><h2>Пользовательское соглашение</h2></DialogTitle>
            <DialogContent>
                <p>
                    Добро пожаловать на платформу TABS!<br/>
                    Это пользовательское соглашение (далее - "Соглашение") устанавливает условия использования платформы
                    TABS (далее - "Платформа"), доступной по адресу [URL] (далее - "Сайт"). Пожалуйста, внимательно
                    ознакомьтесь с ним перед использованием Платформы.
                    <h2>1. Пользовательское соглашение</h2>
                    Используя Платформу, вы соглашаетесь с условиями настоящего Соглашения. <b>Если вы не согласны с
                    этими
                    условиями, пожалуйста, прекратите использование Платформы.</b>
                    <h2>2. Предмет услуг</h2>
                    Платформа предоставляет возможность просмотра, проигрывания и редактирования гитарных табулатур в
                    формате Guitar Pro.<br/>
                    <h2>3. Ответственность за загрузку табулатур</h2>
                    Пользователь несет ответственность за загрузку гитарных табулатур, защищенных авторским правом.
                    Платформа не несет ответственности за нарушение авторских прав со стороны пользователей.<br/>
                    <h2>4. Удаление табулатур по требованию правообладателя</h2>
                    Платформа оставляет за собой право удалять гитарные табулатуры по требованию правообладателя.<br/>
                    <h2>5. Изменения условий</h2>
                    Платформа оставляет за собой право в любое время изменять условия настоящего Соглашения без
                    предварительного уведомления. Использование Платформы после внесения изменений означает ваше
                    согласие с обновленными условиями.
                    <h2>6. Отказ от гарантий</h2>
                    Платформа предоставляется на условиях "как есть". Платформа не гарантирует, что ее услуги будут
                    бесперебойными или безошибочными.
                </p>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleAgree} variant={'contained'}>Согласен</Button>
            </DialogActions>
        </Dialog>
    );
};

export default TermsDialog;
