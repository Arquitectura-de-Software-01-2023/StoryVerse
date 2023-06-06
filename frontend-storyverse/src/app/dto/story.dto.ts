export interface StoryDto {
    storyId: number;
    writerId: number;
    categoryId: number;
    title: string;
    urlCover: string;
    description: string;
    audience: string;
    language: string;
    publicationDate: Date;
    votes: number;
    status: boolean;
}