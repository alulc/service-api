create table if not exists service (
    id int unsigned auto_increment not null,
    uuid binary(16) not null,
    name varchar(255) not null,
    description varchar(255) not null,
    specification_name varchar(255) not null,
    specification_version varchar(255) not null,
    request_count int unsigned not null,
    created_at datetime default current_timestamp,
    primary key (id),
    unique (uuid)
);

create table if not exists endpoint (
    id int unsigned auto_increment not null,
    uuid binary(16) not null,
    service_id int unsigned,
    url varchar(255) not null,
    http_verb enum('GET', 'PUT', 'POST', 'DELETE') not null,
    oauth2_supported bool not null,
    oauth1a_supported bool not null,
    created_at datetime default current_timestamp,
    foreign key (service_id)
        references service(id)
        on delete cascade,
    primary key (id),
    unique (uuid)
);